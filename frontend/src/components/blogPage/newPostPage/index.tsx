import { useEffect, useRef, useState } from 'react';
import { Editor } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';
import PostEditor from './postEditor';

const NewPostMain = function () {
	const [title, setTitle] = useState('');
	const [category, setCategory] = useState('');
	const [thumbnail, setThumbnail] = useState('');
	const [content, setContent] = useState('');
	const editorRef = useRef<Editor>(null);

	useEffect(() => {
		if (editorRef.current) {
			editorRef.current.getInstance().removeHook('addImageBlobHook');
		}

		return () => {};
	}, [editorRef]);

	const onChange = () => {
		const markdown = editorRef.current?.getInstance().getMarkdown();
		setContent(markdown || '');
	};

	const onSave = () => {
		console.log('save!');
	};

	return (
		<div className="new-post-main">
			<button type="button" onClick={onSave}>
				저장
			</button>
			<Editor
				initialValue="hello react editor world!"
				previewStyle="vertical"
				height="600px"
				initialEditType="markdown"
				useCommandShortcut
				ref={editorRef}
				onChange={onChange}
			/>
		</div>
	);
};

export default NewPostMain;
