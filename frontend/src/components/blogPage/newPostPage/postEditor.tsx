import { useEffect, useRef } from 'react';
import { Editor } from '@toast-ui/react-editor';
import axios from 'axios';

type Props = {
	setText: any;
};

const PostEditor = function () {
	return (
		<div className="postEditor">
			<Editor
				onChange={() => {
					console.log('change');
				}}
			/>
		</div>
	);
};

export default PostEditor;
