import usePostList from '../../hooks/post/usePostList';
import PostItem from '../postItem';
import { CircleLoading } from '../../utils/loading/CircularLoading';
import useQueryParam from '../../hooks/useQueryParam';
import usePostFilter from '../../hooks/post/usePostFilter';

const PostListMain = function () {
	const params = useQueryParam();
	const { filterType, filterString } = usePostFilter(params);

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
