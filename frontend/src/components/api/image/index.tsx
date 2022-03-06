import axios, { AxiosRequestConfig } from 'axios';
import config from '../../config';
import { IImageResponseDto } from './types';

const axiosConfig: AxiosRequestConfig = {
	withCredentials: true,
	headers: { 'content-type': 'multipart/formdata' },
};

export const uploadImage = async (image: any) => {
	const formData = new FormData();
	formData.append('image', image);

	const uri = `${config.SERVER_PREFIX}/api/image`;
	const response = await axios.post<IImageResponseDto>(uri, formData, axiosConfig);

	return response.data.url;
};
