import { Post } from '../../api/post/types';
import EditorMain from '../editPost';

const NewPostMain = function () {
	const defaultPost: Post = {
		id: -1,
		title: '제목 없음',
		content: '',
		category: '',
		thumbnail: '',
		createAt: new Date(),
		updateAt: new Date(),
		hashtags: [],
	};

	return <EditorMain post={defaultPost} />;
};

export default NewPostMain;
