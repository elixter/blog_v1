import ReactMarkdown from 'react-markdown';
import { Link } from 'react-router-dom';
import { Post } from '../../api/post/types';
import { timeFormat } from '../../utils';

type Props = {
	post: Post;
};

const PostContent = function ({ post }: Props) {
	return (
		<div className="main">
			<div className="blog-top post-top">
				<img alt="bg" className="bg-img" src={post.thumbnail} />
				<div className="top-content">
					<h1>{post.title}</h1>
					<p>{post.category}</p>
					<p>{timeFormat(post.updateAt)}</p>
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
			</div>
			<div className="post-content">
				<button
					className="back-btn"
					type="button"
					onClick={() => {
						window.history.back();
					}}
				>
					back
				</button>
				<ReactMarkdown>{post.content}</ReactMarkdown>
			</div>
		</div>
	);
};

export default PostContent;
