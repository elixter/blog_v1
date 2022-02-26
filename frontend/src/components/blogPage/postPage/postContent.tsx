import { memo, useEffect, useRef } from 'react';
import { Viewer } from '@toast-ui/react-editor';
import Prism from 'prismjs';
import 'prismjs/themes/prism.css';
import '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css';
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight';
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
				<Viewer ref={viewerRef} plugins={[[codeSyntaxHighlight, { highlighter: Prism }]]} />
			</div>
		</div>
	);
};

export default memo(PostContent);
