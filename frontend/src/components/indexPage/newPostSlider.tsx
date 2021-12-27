import React from 'react';
import Carousel from 'react-material-ui-carousel';
import { Paper, Button } from '@mui/material';

export type PostPreview = {
	id: number;
	title: string;
	thumbnail: string;
};

export type slideItemProps = {
	post: PostPreview;
};

type newPostSliderProps = {
	newPosts: Array<PostPreview>;
};

const SlideItem = function ({ post }: slideItemProps) {
	return (
		<Paper>
			<div
				className="post-slide"
				style={{
					backgroundImage: `url(${post.thumbnail})`,
				}}
			>
				<div className="post-info">
					<h2 className="tit">{post.title}</h2>
					<span>2021-12-27 월</span>
				</div>
			</div>
		</Paper>
	);
};

const NewPostSlider = function ({ newPosts }: newPostSliderProps) {
	return (
		<div className="new-post-slider">
			<Carousel>
				{newPosts.map((newPost, i) => (
					<SlideItem key={newPost.id} post={newPost} />
				))}
			</Carousel>
		</div>
	);
};

export default NewPostSlider;
