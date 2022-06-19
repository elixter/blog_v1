import axios, { AxiosRequestConfig } from 'axios';
import { UserProfile } from '../../authentication/hooks/useAuthentication';
import config from '../../config';
import { LoginParams } from './types';

const axiosConfig: AxiosRequestConfig = {
	withCredentials: true,
};

export const getUserProfile = async () => {
	const res = await axios.get<UserProfile>('/api/user', axiosConfig);
	return res.data;
};

export const loginRequest = async (loginRequest: LoginParams) => {
	const response = await axios.post(`${config.SERVER_PREFIX}/api/auth/signin`, loginRequest, axiosConfig);
	return response.data;
};
