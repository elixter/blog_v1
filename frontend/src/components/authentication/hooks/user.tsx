import axios, { AxiosRequestConfig } from 'axios';
import { UserProfile } from './useAuthentication';
import config from '../../config';
import { LoginParams } from './types';

const axiosConfig: AxiosRequestConfig = {
	withCredentials: true,
};

export const getUserProfile = async () => {
	const res = await axios.get<UserProfile>('/api/users', axiosConfig);
	return res.data;
};

export const loginRequest = async (request: LoginParams) => {
	const response = await axios.post(`${config.SERVER_PREFIX}/api/auth/signin`, request, axiosConfig);
	return response.data;
};
