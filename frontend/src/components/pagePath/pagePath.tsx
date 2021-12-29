export enum StaticPath {
	MAIN = '/',
	PROFILE = '/profile',
	BLOG = '/blog',
}

export enum QueryPath {}

export enum DynamicPath {}

export type PagePathKey = keyof typeof StaticPath;
