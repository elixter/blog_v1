import axios, { AxiosRequestConfig } from 'axios';
import { UserProfile } from '../../authentication/hooks/useAuthentication';

const axiosConfig: AxiosRequestConfig = {
	withCredentials: true,
};

export const getUserProfile = async () => {
	const res = await axios.get<UserProfile>('/api/user', axiosConfig);
	return res.data;
};
