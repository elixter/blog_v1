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