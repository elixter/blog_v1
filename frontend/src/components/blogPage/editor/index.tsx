import { BaseSyntheticEvent, memo, useCallback, useEffect, useRef, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { CreatePostDto, Post, UpdatePostDto } from '../../api/post/types';
import PostEditor from './postEditor';
import Selector from '../../utils/selector';
import HashTagEditor from './hashtagEditor';
import { createPost, updatePost } from '../../api/post';
import { uploadImage } from '../../api/image';

type Props = {
	post: Post;
};

const EditorMain = function ({ post }: Props) {
	const [title, setTitle] = useState(post.title);
	const [category, setCategory] = useState(post.category);
	const [thumbnail, setThumbnail] = useState(post.thumbnail);
	const [content, setContent] = useState(post.content);
	const [hashtags, setHashtags] = useState(post.hashtags);
	const [imageUrlList, setImageUrlList] = useState<Array<string>>([]);

	const thumbnailRef = useRef<HTMLInputElement | null>(null);
	const history = useHistory();
	const options: string[] = ['test1', 'test2'];

	const onTitleChange = useCallback((e: BaseSyntheticEvent) => {
		setTitle(e.target.value);
	}, []);

	const onCategoryChange = useCallback((e: BaseSyntheticEvent) => {
		setCategory(e.target.value);
	}, []);

	const onSave = useCallback(() => {
		if (post.id === -1) {
			const createPostRequestBody: CreatePostDto = {
				title,
				category,
				content,
				thumbnail,
				hashtags,
				imageUrlList,
			};

			console.log(createPostRequestBody);

			createPost(createPostRequestBody).then((res) => {
				if (res.status === 201) {
					history.push(res.headers.location);
				}
			});
		} else {
			const { id } = post;
			const { createAt } = post;
			const updatePostRequestBody: UpdatePostDto = {
				id,
				title,
				category,
				content,
				thumbnail,
				hashtags,
				imageUrlList,
				createAt,
			};

			console.log(updatePostRequestBody);

			updatePost(updatePostRequestBody).then((res) => {
				if (res.status === 200) {
					history.push(res.headers.location);
				}
			});
		}
	}, [category, content, hashtags, history, imageUrlList, post, thumbnail, title]);

	const onChangeImage = useCallback(async () => {
		const input = thumbnailRef.current as HTMLInputElement;
		const image = (input.files && input.files[0]) || null;
		if (!image) {
			return;
		}
		const response = await uploadImage(image);
		setThumbnail(response.url);
	}, []);

	return (
		<div className="editor-main">
			<div className="blog-top post-top">
				<label className="upload-btn" htmlFor="upload-thumb">
					<span>썸네일 변경</span>
					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512">
						<path d="M384 352v64c0 17.67-14.33 32-32 32H96c-17.67 0-32-14.33-32-32v-64c0-17.67-14.33-32-32-32s-32 14.33-32 32v64c0 53.02 42.98 96 96 96h256c53.02 0 96-42.98 96-96v-64c0-17.67-14.33-32-32-32S384 334.3 384 352zM201.4 9.375l-128 128c-12.51 12.51-12.49 32.76 0 45.25c12.5 12.5 32.75 12.5 45.25 0L192 109.3V320c0 17.69 14.31 32 32 32s32-14.31 32-32V109.3l73.38 73.38c12.5 12.5 32.75 12.5 45.25 0s12.5-32.75 0-45.25l-128-128C234.1-3.125 213.9-3.125 201.4 9.375z" />
					</svg>
				</label>
				<input
					className="edit-thumb"
					ref={thumbnailRef}
					type="file"
					accept={'image/*'}
					onChange={onChangeImage}
					id="upload-thumb"
				/>
				<img alt="bg" className="bg-img" src={thumbnail} />
			</div>
			<div className="top-content">
				<h1 className="post-title">
					<input className="input-tit" value={title} placeholder="제목 없음" maxLength={50} onChange={onTitleChange} />
				</h1>
				<Selector defaultValue={category} onChange={onCategoryChange}>
					<option>category1</option>
					<option>category2</option>
					<option>category3</option>
				</Selector>
				<HashTagEditor hashtags={hashtags} setHashtags={setHashtags} />
			</div>
			<div className="main-content">
				<PostEditor content={content} setContent={setContent} images={imageUrlList} setImages={setImageUrlList} />
				<button className="editor-btn" type="button" onClick={onSave}>
					등록
				</button>
				<button
					className="editor-btn cancel"
					type="button"
					onClick={() => {
						window.history.back();
					}}
				>
					취소
				</button>
			</div>
		</div>
	);
};

export default memo(EditorMain);
