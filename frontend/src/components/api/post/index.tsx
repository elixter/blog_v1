import * as queryString from 'querystring';
import axios, { AxiosRequestConfig } from 'axios';
import { GetPostListParams, IGetPostListParams, Posts, Post, CreatePostDto, UpdatePostDto } from './types';
import config from '../../config';

const axiosConfig: AxiosRequestConfig = {
	withCredentials: true,
};

export const getPostList = async (params?: IGetPostListParams) => {
	const uri = `${config.SERVER_PREFIX}/api/posts?${queryString.stringify({
		...new GetPostListParams(params),
	})}`;

	console.log(uri);

	const response = await axios.get<Posts>(uri, axiosConfig);
	return response.data;
};

export const getPost = async (postId?: string) => {
	const uri = `${config.SERVER_PREFIX}/api/posts/${postId}`;

	const response = await axios.get<Post>(uri, axiosConfig);
	return response.data;
};

export const createPost = async (postDto: CreatePostDto) => {
	const uri = `${config.SERVER_PREFIX}/api/posts`;

	const response = await axios.post(uri, postDto, axiosConfig);
	console.log(response);
	return response;
};

export const updatePost = async (postDto: UpdatePostDto) => {
	const uri = `${config.SERVER_PREFIX}/api/posts`;

	const response = await axios.put(uri, postDto, axiosConfig);
	return response;
};
