import { Link } from 'react-router-dom';
import { memo, useState } from 'react';
import PostItem from '../postItem';
import { DEFAULT_PAGE_SIZE, GetPostListParams } from '../../api/post/types';
import usePostList from '../../hooks/post/usePostList';
import { CircleLoading } from '../../utils/loading/CircularLoading';
import { StaticPath } from '../../pagePath/pagePath';

type Props = {
	categoryName: string;
};

const BlogMainPostList = function ({ categoryName }: Props) {
	const [postListParams, setPostListParams] = useState(
		new GetPostListParams({
			page: 0,
			size: DEFAULT_PAGE_SIZE,
			filterType: 'category',
			filterString: categoryName,
		})
	);
	const { data, mutate } = usePostList({
		params: postListParams,
	});

	console.log(data?.posts);

	return (
		<div className="post-list">
			<div className="post-list-hd">
				<h1>{categoryName}</h1>
				<Link className="more" to={`/blog/posts?category=${categoryName}`}>
					All Articles
				</Link>
			</div>
			<div className="post-list-items">
				{(data &&
					data.posts.map((post, i) => {
						return <PostItem key={post.id} post={post} />;
					})) || <CircleLoading />}
			</div>
		</div>
	);
};

export default memo(BlogMainPostList);
