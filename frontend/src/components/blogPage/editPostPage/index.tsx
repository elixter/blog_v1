import usePost from '../../hooks/post/usePost';
import EditorMain from '../editor';
import { CircleLoading } from '../../utils/loading/CircularLoading';

const EditPostMain = function () {
	const { post } = usePost();

	return (post && <EditorMain post={post} />) || <CircleLoading />;
};

export default EditPostMain;
