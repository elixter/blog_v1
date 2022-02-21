import { EditorContent, useEditor } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import { memo, useEffect } from 'react';
import { Post } from '../../api/post/types';
import PostContentTop from './postContentTop';

type Props = {
	post: Post;
};

const PostContent = function ({ post }: Props) {
	const editor = useEditor({
		extensions: [
			StarterKit.configure({
				heading: {
					levels: [1, 2, 3],
				},
			}),
		],
		editorProps: {
			attributes: {
				class: 'WYSIWYG-editor',
			},
		},
		content: `${post.content}`,
		editable: false,
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
				{/* <div>{post.content}</div> */}
				<div className="postEditor">
					<EditorContent editor={editor} />
				</div>
			</div>
		</div>
	);
};

export default memo(PostContent);
