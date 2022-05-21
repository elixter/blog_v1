import { useEffect, useState } from 'react';
import usePostList from '../../hooks/post/usePostList';
import PostItem from '../postItem';
import { CircleLoading } from '../../utils/loading/CircularLoading';
import useQueryParam from '../../hooks/useQueryParam';
import usePostFilter from '../../hooks/post/usePostFilter';

const PostListMain = function () {
	const params = useQueryParam();
	const { filterType, filterString } = usePostFilter(params);

	const [page, setPage] = useState(0);

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
		<div className="main">
			<div className="blog-top">
				{/* <img */}
				{/*	alt="bg" */}
				{/*	className="bg-img" */}
				{/*	src="https://www.gousa.or.kr/sites/default/files/styles/hero_xl_1600x700/public/images/hero_media_image/2017-01/Getty_515070156_EDITORIALONLY_LosAngeles_HollywoodBlvd_Web72DPI_0.jpg?itok=hxCEUSBf" */}
				{/* /> */}
				<div className="top-content">
					<h1>{filterString}</h1>
				</div>
			</div>

			<div className="blog-content">
				<div className="post-list-items-flex flex-v">
					{(data &&
						data.posts.map((post, i) => {
							return <PostItem key={post.id} post={post} />;
						})) || <CircleLoading />}
				</div>
			</div>
		</div>
	);
};

export default PostListMain;
