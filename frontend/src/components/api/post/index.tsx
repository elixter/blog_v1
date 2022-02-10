import * as queryString from 'querystring';
import axios, { AxiosRequestConfig } from 'axios';
import { GetPostListParams, IGetPostListParams, Posts } from './types';
import config from '../../config';

const axiosConfig: AxiosRequestConfig = {
	withCredentials: true,
};

export const getPostList = async (params?: IGetPostListParams) => {
	const uri = `${config.SERVER_PREFIX}/api/posts?${queryString.stringify({
		...new GetPostListParams(params),
	})}`;

	const response = await axios.get<Posts>(uri, axiosConfig);
	return response.data;
};
