import { useEffect } from 'react';
import { atom, useRecoilState } from 'recoil';
import { AxiosError } from 'axios';
import useSWR from 'swr';
import { getUserProfile } from '../../api/user';
import { UserProfile } from '../../authentication/hooks/useAuthentication';

export enum UserType {
	Login = 'Login',
	SignUp = 'SignUp',
	Visitor = 'Visitor',
}

export type User = {
	type: UserType;
	profile: UserProfile | null;
	isAuthentication: boolean;
} | null;

const Authentication = atom<User>({
	key: 'Authentication',
	default: null,
});

const useAuthentication = () => {
	const [user, setUser] = useRecoilState(Authentication);

	const result = useSWR(
		() => 'GetUserProfile',
		async () => {
			try {
				const response = await getUserProfile();
				return response;
			} catch (e: AxiosError | any) {
				if (e && e.isAxiosError) {
					const status = (e as AxiosError).response?.status;
					if (status === 401 || status === 404) {
						// 인증 실패 등 추가 처리 필요
						return null;
					}

					return null;
				}
				throw e;
			}
		}
	);

	const { data, error, mutate } = result;

	useEffect(() => {
		if (data !== undefined) {
			setUser({
				type: (data && UserType.Login) || UserType.Visitor,
				profile: data,
				isAuthentication: !!data,
			});
		}
	}, [data, setUser]);

	return {
		loading: !error && !user,
		error,
		user,
		setUser,
		mutate,
	};
};

export default useAuthentication;
