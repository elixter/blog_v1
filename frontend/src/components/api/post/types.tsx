export interface IPostDto {
	id: number;
	title: string;
	content: string;
	category: string;
	thumbnail: string;
	createAt: Date;
	updateAt: Date;
	hashtags: Array<string>;
}

export interface IGetPostListParams {
	curPage?: string;
	pageSize: number;
	sort?: string;
	filterType?: string;
	filterString?: string;
}

export const DEFAULT_PAGE_SIZE = 4;

export class GetPostListParams implements IGetPostListParams {
	curPage: string;

	pageSize: number;

	sort: string;

	filterType: string;

	filterString: string;

	constructor(postListParams?: IGetPostListParams) {
		this.curPage = postListParams?.curPage || '';
		this.pageSize = postListParams?.pageSize || DEFAULT_PAGE_SIZE;
		this.sort = postListParams?.sort || '';
		this.filterType = postListParams?.filterType || '';
		this.filterString = postListParams?.filterString || '';
	}
}

export interface Pagination {
	curPage: number;
	pageSize: number;
	lastPage: number;
	itemCount: number;
}

export interface Posts {
	posts: Post[];
	pagination: Pagination;
}

export interface Post {
	id: number;
	title: string;
	content: string;
	category: string;
	thumbnail: string;
	createAt: Date;
	updateAt: Date;
	hashtags: Array<string>;
}

export interface CreatePostDto {
	title: string;
	content: string;
	category: string;
	thumbnail: string;
	hashtags: Array<string>;
}
