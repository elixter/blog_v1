import useSWR from 'swr';
import { Post } from '../../api/post/types';
import usePostLocation from './usePostLocation';
import { getPost } from '../../api/post';
import { sleep } from '../../utils';

const usePost = () => {
	const { postId } = usePostLocation();
	const result = useSWR(
		() => {
			return ['getPost', postId];
		},
		async (key, post) => {
			const delayedData = await sleep(500).then(async () => {
				const response = await getPost(postId);
				return response;
			});
			return delayedData;
		},
		{
			revalidateOnFocus: false,
		}
	);
	return {
		...result,
		post: result?.data,
		loading: !result.data && !result.error,
	};
};

export default usePost;
