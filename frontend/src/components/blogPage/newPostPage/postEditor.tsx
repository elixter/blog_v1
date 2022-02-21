import { Editor, EditorContent } from '@tiptap/react';

type Props = {
	editor: Editor | null;
};

const PostEditor = function ({ editor }: Props) {
	return (
		<div className="postEditor">
			<EditorContent editor={editor} />
		</div>
	);
};

export default PostEditor;
