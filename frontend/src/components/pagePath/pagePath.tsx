export enum StaticPath {
	MAIN = '/',
	PROFILE = '/profile',
	BLOG = '/blog',
	BLOG_NEW_POST = '/blog/new',
}

export enum DynamicPath {
	BLOG_POST_DETAIL = '/blog/posts/:postId',
}

export enum QueryPath {}

export type PagePathKey = keyof typeof StaticPath;
