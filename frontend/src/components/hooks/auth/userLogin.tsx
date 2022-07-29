import { useCallback, useEffect } from 'react';
import { AxiosError } from 'axios';
import { atom, useRecoilState } from 'recoil';
import { useSnackbar } from 'notistack';
import { LoginParams } from '../../authentication/hooks/types';
import { sleep } from '../../utils';
import { loginRequest } from '../../api/user';

type LoginRequestResult = {
	error: AxiosError | null;
	data: unknown;
	loading: boolean;
} | null;

export const loginRequestResult = atom<LoginRequestResult>({
	key: 'loginRequestResult',
	default: null,
});

export const useLogin = () => {
	const [result, setResult] = useRecoilState(loginRequestResult);
	const { enqueueSnackbar } = useSnackbar();

	const fetch = useCallback(
		async (param: LoginParams) => {
			setResult({
				error: null,
				data: null,
				loading: true,
			});

			const state = sleep(300).then(async () => {
				try {
					const response = await loginRequest(param);
					setResult(() => ({
						loading: false,
						error: null,
						data: response,
					}));
					return true;
				} catch (e: AxiosError | any) {
					setResult({
						data: null,
						loading: false,
						error: e,
					});
					enqueueSnackbar('아이디 혹은 비밀번호가 일치하지 않습니다.', {
						variant: 'error',
					});
					return false;
				}
			});

			return state;
		},

		[enqueueSnackbar, setResult]
		// [setResult]
	);

	useEffect(() => {
		return () => {
			setResult(null);
		};
	}, [setResult]);

	return {
		fetch,
		...result,
		loading: result?.loading,
	};
};

export default useLogin;
