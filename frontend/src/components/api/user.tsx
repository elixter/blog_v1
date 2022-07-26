import axios, { AxiosRequestConfig } from 'axios';
import { UserProfile } from '../authentication/hooks/useAuthentication';
import config from '../config';
import { LoginParams, SignupParams } from '../authentication/hooks/types';

const axiosConfig: AxiosRequestConfig = {
	withCredentials: true,
};

export const getUserProfile = async () => {
	const res = await axios.get<UserProfile>(`${config.SERVER_PREFIX}/api/users`, axiosConfig);
	return res.data;
};

export const loginRequest = async (request: LoginParams) => {
	const response = await axios.post(`${config.SERVER_PREFIX}/api/auth/signin`, request, axiosConfig);
	return response.data;
};

export const signupRequest = async (signUpParams: SignupParams) => {
	try {
		const response = await axios.post(`${config.SERVER_PREFIX}/api/users`, signUpParams, axiosConfig);
		return response.data;
	} catch (e: any) {
		if (e.response.status === 409) {
			throw new Error('이미 사용중인 이메일입니다.');
		} else if (e.response.status >= 400) {
			throw new Error('알수없는 에러입니다.');
		}
		return null;
	}
};
