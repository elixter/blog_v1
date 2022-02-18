export enum StaticPath {
	MAIN = '/',
	PROFILE = '/profile',
	BLOG = '/blog',
}

export enum DynamicPath {
	BLOG_POST_DETAIL = '/blog/posts/:postId',
}

export enum QueryPath {}

export type PagePathKey = keyof typeof StaticPath;
