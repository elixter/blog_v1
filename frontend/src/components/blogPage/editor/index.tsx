import { BaseSyntheticEvent, memo, useCallback, useEffect, useState } from 'react';
import { Post } from '../../api/post/types';
import PostEditor from './postEditor';
import Selector from '../../utils/selector';
import HashTagEditor from './hashtagEditor';

type Props = {
	post: Post;
};

// TODO: 카테고리 drop menu, 썸네일 업로드 아이콘 및 기타 처리

const EditorMain = function ({ post }: Props) {
	const [title, setTitle] = useState(post.title);
	const [category, setCategory] = useState(post.category);
	const [thumbnail, setThumbnail] = useState(post.thumbnail);
	const [content, setContent] = useState(post.content);
	const [hashtags, setHashtags] = useState(post.hashtags);

	const options: string[] = ['test1', 'test2'];

	const onTitleChange = useCallback((e: BaseSyntheticEvent) => {
		setTitle(e.target.value);
	}, []);

	const onCategoryChange = useCallback((e: BaseSyntheticEvent) => {
		setCategory(e.target.value);
	}, []);

	return (
		<div className="editor-main">
			<div className="blog-top post-top">
				<img alt="bg" className="bg-img" src={post.thumbnail} />
			</div>
			<div className="top-content">
				<h1 className="post-title">
					<input className="input-tit" placeholder="제목 없음" maxLength={50} onChange={onTitleChange} />
				</h1>
				<Selector defaultValue={category} onChange={onCategoryChange}>
					<option>category1</option>
					<option>category2</option>
					<option>category3</option>
				</Selector>
				<HashTagEditor hashtags={hashtags} setHashtags={setHashtags} />
			</div>
			<div className="main-content">
				<PostEditor content={content} setContent={setContent} />
			</div>
		</div>
	);
};

export default memo(EditorMain);
