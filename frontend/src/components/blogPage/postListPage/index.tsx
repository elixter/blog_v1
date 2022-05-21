import usePostList from '../../hooks/post/usePostList';
import PostItem from '../postItem';
import { CircleLoading } from '../../utils/loading/CircularLoading';
import useQueryParam from '../../hooks/post/useQueryParam';

const PostListMain = function () {
	const params = useQueryParam();

	console.log(params);

	let filterString: string | undefined = '';
	let filterType: string | undefined = '';

	Object.keys(params).forEach((key) => {
		if (key === 'category' || key === 'hashtag') {
			filterType = key as string | undefined;
			filterString = params[key] as string | undefined;
		}
	});

	const { data } = usePostList({
		params: {
			page: 0,
			size: 10,
			filterType,
			filterString,
		},
	});

	console.log(data);

	return (
		<div className="post-list-items">
			{(data &&
				data.posts.map((post, i) => {
					return <PostItem key={post.id} post={post} />;
				})) || <CircleLoading />}
		</div>
	);
};

export default PostListMain;
