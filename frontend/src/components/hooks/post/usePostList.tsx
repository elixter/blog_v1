import useSWR from 'swr';
import { AxiosError } from 'axios';
import { IGetPostListParams, Posts } from '../../api/post/types';
import { getPostList } from '../../api/post';
import { sleep } from '../../utils';

type Props =
	| {
			params?: IGetPostListParams;
	  }
	| undefined;

const usePostList = (props: Props = undefined) => {
	const result = useSWR<Posts, AxiosError>(
		() => ['getPostResult', props?.params],
		async (key, value) => {
			const data = await sleep(300).then(async () => {
				const delayedData = await getPostList(value);
				return delayedData;
			});
			return data;
		}
	);

	return {
		loading: !result.data,
		...result,
	};
};

export default usePostList;
