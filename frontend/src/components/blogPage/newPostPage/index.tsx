import { Post } from '../../api/post/types';
import EditorMain from '../editor';

export const defaultPost: Post = {
	id: -1,
	title: '',
	content: '',
	category: '',
	thumbnail: '',
	createAt: new Date(),
	updateAt: new Date(),
	hashtags: [],
	imageUrlList: [],
};

const NewPostMain = function () {
	return <EditorMain post={defaultPost} />;
};

export default NewPostMain;
