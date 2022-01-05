import React from 'react';
import NewPostSlider, { PostPreview } from './newPostSlider';
import IndexContent from './indexContent';

const testData: PostPreview[] = [
	{
		id: 1,
		title: 'test1',
		uploadedAt: '2021-12-28',
		thumbnail:
			'https://ichef.bbci.co.uk/news/640/cpsprodpb/8A64/production/_121882453_e22f709d-613a-4872-b532-3d5667aa6860.jpg',
		summary: 'test data 1',
	},
	{
		id: 2,
		title: 'test2',
		uploadedAt: '2021-12-28',
		thumbnail:
			'https://image.cnbcfm.com/api/v1/image/106092057-1566487914671gettyimages-1095029036.jpeg?v=1614637159&w=630&h=354',
		summary: 'test data 2',
	},
	{
		id: 3,
		title: 'test',
		uploadedAt: '2021-12-28',
		thumbnail: 'https://storage.googleapis.com/afs-prod/media/cd97618a6631435e851aaf9520d6a616/800.jpeg',
		summary: 'test data 3',
	},
];

const IndexMain = function () {
	return (
		<div className="main">
			<NewPostSlider newPosts={testData} />
			<IndexContent />
		</div>
	);
};

export default IndexMain;
