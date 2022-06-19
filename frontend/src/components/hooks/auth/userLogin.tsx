import { useCallback, useEffect } from 'react';
import { AxiosError } from 'axios';
import { atom, useRecoilState } from 'recoil';
import { LoginParams } from '../../api/auth/types';
import { sleep } from '../../utils';
import { loginRequest } from '../../api/auth/user';

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
					return false;
				}
			});

			return state;
		},

		[setResult]
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
