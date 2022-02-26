import { Dispatch, SetStateAction, useEffect, useRef } from 'react';
import { Editor } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';

type Props = {
	content: string;
	setContent: Dispatch<SetStateAction<string>>;
};

const PostEditor = function ({ content, setContent }: Props) {
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

	return (
		<div className="postEditor">
			<Editor
				initialValue={content}
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

export default PostEditor;
