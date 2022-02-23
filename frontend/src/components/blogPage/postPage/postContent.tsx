import { memo, useEffect, useRef } from 'react';
import { Viewer } from '@toast-ui/react-editor';
import { Post } from '../../api/post/types';
import PostContentTop from './postContentTop';

type Props = {
	post: Post;
};

const PostContent = function ({ post }: Props) {
	const viewerRef = useRef<Viewer>(null);

	useEffect(() => {
		if (viewerRef.current) {
			viewerRef.current.getInstance().setMarkdown(post.content);
		}
	});

	return (
		<div className="main">
			<PostContentTop post={post} />
			<div className="post-content">
				<button
					className="back-btn"
					type="button"
					onClick={() => {
						window.history.back();
					}}
				>
					back
				</button>
				<Viewer ref={viewerRef} />
			</div>
		</div>
	);
};

export default memo(PostContent);
