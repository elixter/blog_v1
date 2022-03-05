import { Dispatch, memo, SetStateAction, useCallback, useEffect, useRef } from 'react';
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

import axios from 'axios';
import Prism from '../../prism';
import config from '../../config';

type Props = {
	content: string;
	setContent: Dispatch<SetStateAction<string>>;
};

const PostEditor = function ({ content, setContent }: Props) {
	const editorRef = useRef<Editor>(null);

	const imageBlobHook = (blob: any, callback: any) => {
		(async () => {
			const formData = new FormData();
			formData.append('image', blob);
			const response = await axios.post(`${config.SERVER_PREFIX}/api/image`, formData, {
				withCredentials: true,
				headers: { 'content-type': 'multipart/formdata' },
			});

			callback(response.data.url, '');
		})();
	};

	const onChange = useCallback(() => {
		const markdown = editorRef.current?.getInstance().getMarkdown();
		setContent(markdown || '');
	}, [setContent]);

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
				hooks={{
					addImageBlobHook: imageBlobHook,
				}}
			/>
		</div>
	);
};

export default memo(PostEditor);
