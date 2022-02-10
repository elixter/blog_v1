import PostList from './postList';

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
				</div>
			</div>
			<div className="blog-content">
				<PostList categoryName="category1" />
				<PostList categoryName="category2" />
				<PostList categoryName="category3" />
			</div>
		</div>
	);
};

export default BlogMain;
