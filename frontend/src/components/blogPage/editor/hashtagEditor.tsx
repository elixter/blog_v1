import { Dispatch, memo, SetStateAction, useCallback, useState } from 'react';
import HashTagItem from './hashtagItem';
import HashtagList from './hashtagList';

type Props = {
	hashtags: string[];
	setHashtags: Dispatch<SetStateAction<string[]>>;
};

const HashtagEditor = function ({ hashtags, setHashtags }: Props) {
	const [tag, setTag] = useState('');

	const onKeyDown = useCallback(
		(e) => {
			if (e.keyCode === 32 || e.keyCode === 13 || e.key === ' ') {
				if (tag === '' || tag === ' ') {
					e.target.value = null;
					setTag('');
					return;
				}
				if (hashtags.includes(tag)) {
					// clear if already have same tag.
					e.target.value = null;
					setTag('');
					return;
				}

				setHashtags([...hashtags, tag]);
				setTag('');
				e.target.value = '';
			}
		},
		[hashtags, setHashtags, tag]
	);

	const onChange = useCallback((e) => {
		if (e.target.value === ' ') {
			e.target.value = null;
			setTag('');
		} else {
			setTag(e.target.value);
		}
	}, []);

	return (
		<div className="hashtags">
			<HashtagList hashtags={hashtags} setHashtags={setHashtags} />
			<input
				className="input-hashtag"
				placeholder="태그 추가"
				maxLength={30}
				onChange={onChange}
				onKeyDown={onKeyDown}
			/>
		</div>
	);
};

export default memo(HashtagEditor);
