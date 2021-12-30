import { Link } from 'react-router-dom';
import Card from '../utils/gridCard/card';
import Grid from '../utils/gridCard/gird';

const BlogMain = function () {
	return (
		<div className="main">
			<div className="blog-top">
				<img
					alt="bg"
					className="bg-img"
					src="https://www.gousa.or.kr/sites/default/files/styles/hero_xl_1600x700/public/images/hero_media_image/2017-01/Getty_515070156_EDITORIALONLY_LosAngeles_HollywoodBlvd_Web72DPI_0.jpg?itok=hxCEUSBf"
				/>
				<div className="top-content">
					<h1>Blog</h1>
					<span>What i study, what i do</span>
				</div>
			</div>
			<div className="blog-content">
				<div className="post-list">
					<div className="post-list-hd">
						<h1>Category 1</h1>
						<Link className="more" to="">
							All Articles
						</Link>
					</div>
					<div className="post-list-items">
						<div className="post-list-item">
							<img
								alt="thumbnail"
								src="https://www.gousa.or.kr/sites/default/files/styles/hero_xl_1600x700/public/images/hero_media_image/2017-01/Getty_515070156_EDITORIALONLY_LosAngeles_HollywoodBlvd_Web72DPI_0.jpg?itok=hxCEUSBf"
							/>
							<div className="tit">
								<h2>Title</h2>
							</div>
							<div className="post-summary">
								<span>
									David Carlson didn&apos;t really like his daughter staying for hours at his house. The house was very
									&quot;MUCKY&quot; and rotting food spilled in the side alley next to it which attracted ...
								</span>
							</div>
							<div className="post-item-bt">
								<div className="hashtags">
									<Link to="">#hashtag</Link>
								</div>
								<Link className="post-more" to="">
									Read more
								</Link>
							</div>
						</div>
						<div className="post-list-item">
							<img
								alt="thumbnail"
								src="https://www.gousa.or.kr/sites/default/files/styles/hero_xl_1600x700/public/images/hero_media_image/2017-01/Getty_515070156_EDITORIALONLY_LosAngeles_HollywoodBlvd_Web72DPI_0.jpg?itok=hxCEUSBf"
							/>
							<div className="tit">
								<h2>Title</h2>
							</div>
							<div className="post-summary">
								<span>
									David Carlson didn&apos;t really like his daughter staying for hours at his house. The house was very
									&quot;MUCKY&quot; and rotting food spilled in the side alley next to it which attracted ...
								</span>
							</div>
							<div className="post-item-bt">
								<div className="hashtags">
									<Link to="">#hashtag</Link>
								</div>
								<Link className="post-more" to="">
									Read more
								</Link>
							</div>
						</div>
						<div className="post-list-item">
							<img
								alt="thumbnail"
								src="https://www.gousa.or.kr/sites/default/files/styles/hero_xl_1600x700/public/images/hero_media_image/2017-01/Getty_515070156_EDITORIALONLY_LosAngeles_HollywoodBlvd_Web72DPI_0.jpg?itok=hxCEUSBf"
							/>
							<div className="tit">
								<h2>Title</h2>
							</div>
							<div className="post-summary">
								<span>
									David Carlson didn&apos;t really like his daughter staying for hours at his house. The house was very
									&quot;MUCKY&quot; and rotting food spilled in the side alley next to it which attracted ...
								</span>
							</div>
							<div className="post-item-bt">
								<div className="hashtags">
									<Link to="">#hashtag</Link>
								</div>
								<Link className="post-more" to="">
									Read more
								</Link>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};

export default BlogMain;
