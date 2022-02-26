import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Post } from '../../api/post/types';
import PostEditor from './postEditor';

type Props = {
	post: Post;
};

// TODO: 카테고리 drop menu, 썸네일 업로드 아이콘 및 기타 처리

const EditorMain = function ({ post }: Props) {
	const [title, setTitle] = useState(post.title);
	const [category, setCategory] = useState(post.category);
	const [thumbnail, setThumbnail] = useState(post.thumbnail);
	const [content, setContent] = useState(post.content);

	return (
		<div className="main">
			<div className="blog-top post-top">
				<img alt="bg" className="bg-img" src={post.thumbnail} />
			</div>
			<div className="top-content">
				<h1 className="post-title">
					<textarea placeholder={title} maxLength={50} />
				</h1>
				<div>
					<p>{post.category}</p>
				</div>
				<div className="hashtags">
					{post.hashtags.map((hashtag, i) => {
						return (
							<Link
								key={hashtag + post.id}
								className="hashtag"
								to={`/blog/posts?hashtag=${hashtag}`}
							>{`#${hashtag}`}</Link>
						);
					})}
				</div>
			</div>
			<PostEditor content={content} setContent={setContent} />
		</div>
	);
};

export default EditorMain;
