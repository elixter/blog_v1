import { memo } from 'react';
import HashTagItem from './hashtagItem';

type Props = {
	hashtags: string[];
};

const HashtagList = function ({ hashtags }: Props) {
	return (
		<>
			{hashtags.map((hashtag, i) => {
				return <HashTagItem hashtag={hashtag} key={hashtag} />;
			})}
		</>
	);
};

export default memo(HashtagList, (prevProps, nextProps) => prevProps.hashtags === nextProps.hashtags);
