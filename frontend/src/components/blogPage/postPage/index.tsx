import { memo } from 'react';
import { Post } from '../../api/post/types';
import PostContent from './postContent';
import usePost from '../../hooks/usePost';
import { CircleLoading } from '../../utils/loading/CircularLoading';

const PostMain = function () {
	const { data } = usePost();

	return <div>{(data && <PostContent post={data} />) || <CircleLoading />}</div>;
};

export default memo(PostMain);
