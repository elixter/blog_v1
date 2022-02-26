import { Dispatch, SetStateAction, useEffect, useRef } from 'react';
import 'prismjs/components/prism-java';
import 'prismjs/components/prism-scala';
import 'prismjs/components/prism-go';
import 'prismjs/themes/prism.css';

import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/react-editor';

import '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css';
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight';

import 'tui-color-picker/dist/tui-color-picker.css';
import '@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';

import Prism from '../../prism';

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

		console.log(Prism.languages);

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
				plugins={[[codeSyntaxHighlight, { highlighter: Prism }], colorSyntax]}
				onChange={onChange}
			/>
		</div>
	);
};

export default PostEditor;
