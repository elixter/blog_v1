import { Link } from 'react-router-dom';
import { memo, useEffect } from 'react';

type Props = {
	hashtag: string;
};

const HashtagItem = function ({ hashtag }: Props) {
	useEffect(() => {}, []);

	return (
		<div className="hashtag-wrap">
			<Link className="hashtag" to={`/blog/posts?hashtag=${hashtag}`}>{`#${hashtag}`}</Link>
		</div>
	);
};

export default memo(HashtagItem);
