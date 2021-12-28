import React from 'react';
import Carousel from 'react-material-ui-carousel';
import { Paper, Button } from '@mui/material';
import { faAngleLeft, faAngleRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { makeStyles } from '@mui/styles';

const useStyle = makeStyles({
	prevIcon: {
		width: '25px',
		height: '25px',
		marginLeft: '3%',
		color: 'white',
		alignItems: 'center',
		justifyContent: 'center',
		lineHeight: '25px',
	},
	nextIcon: {
		width: '25px',
		height: '25px',
		marginLeft: '3%',
		color: 'white',
		alignItems: 'center',
		justifyContent: 'center',
		lineHeight: '25px',
	},
});

export type PostPreview = {
	id: number;
	title: string;
	thumbnail: string;
	uploadedAt: string;
	summary: string;
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
				<div className="slide-tit">
					<h2>최근 게시물</h2>
				</div>
				<div className="post-info">
					<h2 className="tit">{post.title}</h2>
					<span>2021-12-27 월</span>
				</div>
				<div className="post-summary">{post.summary}</div>
			</div>
		</Paper>
	);
};

const NextButton = function () {
	const style = useStyle();

	return (
		<button className={style.nextIcon} type="button">
			<FontAwesomeIcon icon={faAngleRight} />
		</button>
	);
};

const PrevButton = function () {
	const style = useStyle();

	return (
		<button className={style.nextIcon} type="button">
			<FontAwesomeIcon icon={faAngleLeft} />
		</button>
	);
};

const NewPostSlider = function ({ newPosts }: newPostSliderProps) {
	return (
		<div className="new-post-slider">
			<Carousel
				indicators={false}
				navButtonsWrapperProps={{ style: { padding: '0 2% 0 2%' } }}
				NextIcon={<NextButton />}
				PrevIcon={<PrevButton />}
			>
				{newPosts.map((newPost, i) => (
					<SlideItem key={newPost.id} post={newPost} />
				))}
			</Carousel>
		</div>
	);
};

export default NewPostSlider;
