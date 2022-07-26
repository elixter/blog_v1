export enum StaticPath {
	MAIN = '/',
	SIGNUP = '/signup',
	PROFILE = '/profile',
	BLOG = '/blog',
	BLOG_NEW_POST = '/blog/new',
}

export enum DynamicPath {
	BLOG_POST_DETAIL = '/blog/posts/:postId',
	BLOG_POST_EDIT = '/blog/posts/:postId/edit',
	BLOG_POST_LIST = '/blog/posts/:key/:value',
}

export enum QueryPath {
	BLOG_POST_LIST = '/blog/posts',
}

export type PagePathKey = keyof typeof StaticPath;
