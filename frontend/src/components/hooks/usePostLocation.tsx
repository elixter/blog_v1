import { useRouteMatch } from 'react-router-dom';

const usePostLocation = () => {
	const match = useRouteMatch<{ postId: string }>();
	return { postId: match.params.postId };
};

export default usePostLocation;
