import { atom, useRecoilState } from 'recoil';
import useSWR from 'swr';
import { AxiosError } from 'axios';
import { useEffect } from 'react';
import { getUserProfile } from '../../api/user';

export enum UserType {
	Login = 'Login',
	SignUp = 'SignUp',
	Visitor = 'Visitor',
}

export type UserProfile = {
	email: string;
	loginId: string;
	name: string;
	profileImage: string;
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
