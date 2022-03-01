import { atom, useRecoilState } from 'recoil';
import useSWR from 'swr';
import { AxiosError } from 'axios';
import { useEffect } from 'react';
import { getUserProfile } from '../../api/auth/user';

export enum UserType {
	Login = 'Login',
	SignUp = 'SignUp',
	Visitor = 'Visitor',
}

export type ProfileImage = {
	url: string;
};

export type UserProfile = {
	name: string;
	profileImage: ProfileImage;
};

export type User = {
	type: UserType;
	profile: UserProfile | null;
	isAuthenticated: boolean;
} | null;

const Authentication = atom<User>({
	key: 'Authentication',
	default: null,
});

const useAuthentication = () => {
	const [user, setUser] = useRecoilState(Authentication);

	const result = useSWR(
		() => 'api/user',
		async () => {
			try {
				const res = await getUserProfile();
				return res;
			} catch (e: AxiosError | any) {
				if (e && e.isAxiosError) {
					const status = (e as AxiosError).response?.status;
					if (status === 401 || status === 404) {
						return null;
					}
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
				isAuthenticated: !!data,
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
