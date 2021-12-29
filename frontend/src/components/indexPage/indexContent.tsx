import Grid from '../utils/gridCard/gird';
import Card from '../utils/gridCard/card';

const IndexContent = function () {
	return (
		<div className="main-content">
			<h1 className="tit">Elixter&apos;s personal platform</h1>
			<p className="txt">This website is built for web fullstack study. I want to create scalable website.</p>
			<div className="content-info">
				<Grid>
					<Card>
						<img
							alt=""
							src="https://a.cdn-hotels.com/gdcs/production144/d1104/44c1f250-8f10-11e8-9bad-0242ac110002.jpg?impolicy=fcrop&w=1600&h=1066&q=medium"
						/>
						<article className="content">
							<h3 className="tit">Blog</h3>
							<p>My personal blog for tech, movie, basketball etc...</p>
						</article>
					</Card>
					<Card>
						<img
							alt=""
							src="https://a.cdn-hotels.com/gdcs/production81/d38/4a7461b0-8f10-11e8-b6b0-0242ac110007.jpg?impolicy=fcrop&w=1600&h=1066&q=medium"
						/>
						<article className="content">
							<h3 className="tit">Project</h3>
							<p>Displaying my personal projects</p>
						</article>
					</Card>
					<Card>
						<img
							alt=""
							src="https://a.cdn-hotels.com/gdcs/production187/d231/629d8820-8f10-11e8-b6b0-0242ac110007.jpg?impolicy=fcrop&w=1600&h=1066&q=medium"
						/>
						<article className="content">
							<h3 className="tit">Chat</h3>
							<p>Simple chatting with other users</p>
						</article>
					</Card>
				</Grid>
			</div>
		</div>
	);
};

export default IndexContent;
