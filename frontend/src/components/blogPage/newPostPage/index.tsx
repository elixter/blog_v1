import { EditorContent, useEditor } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import { Highlight } from '@tiptap/extension-highlight';
import { Typography } from '@tiptap/extension-typography';
import { Blockquote } from '@tiptap/extension-blockquote';
import { Color } from '@tiptap/extension-color';
import { Dropcursor } from '@tiptap/extension-dropcursor';
import { useEffect, useState } from 'react';
import PostEditor from './postEditor';

const NewPostMain = function () {
	const editor = useEditor({
		extensions: [
			StarterKit.configure({
				heading: {
					levels: [1, 2, 3],
				},
			}),
			Highlight,
			Typography,
			Blockquote,
			Color,
			Dropcursor,
		],
		editorProps: {
			attributes: {
				class: 'WYSIWYG-editor',
			},
		},
		content: '<p>Hello World!</p>',
	});
	const [title, setTitle] = useState('');
	const [category, setCategory] = useState('');
	const [thumbnail, setThumbnail] = useState('');

	const onSave = () => {
		const content = editor?.getHTML();
		console.log(content);
	};

	return (
		<div className="new-post-main">
			<button type="button" onClick={onSave}>
				저장
			</button>
			<PostEditor editor={editor} />
		</div>
	);
};

export default NewPostMain;
