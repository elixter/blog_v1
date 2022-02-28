import { Dispatch, memo, SetStateAction } from 'react';
import HashTagItem from './hashtagItem';

type Props = {
	hashtags: string[];
	setHashtags: Dispatch<SetStateAction<string[]>>;
};

const HashtagList = function ({ hashtags, setHashtags }: Props) {
	return (
		<>
			{hashtags.map((hashtag, i) => {
				return <HashTagItem hashtag={hashtag} key={hashtag} />;
			})}
		</>
	);
};

export default memo(HashtagList, (prevProps, nextProps) => prevProps.hashtags === nextProps.hashtags);
