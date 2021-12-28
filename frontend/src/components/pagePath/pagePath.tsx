export enum StaticPath {
	MAIN = '/',
	PROFILE = '/profile',
}

export enum QueryPath {}

export enum DynamicPath {}

export type PagePathKey = keyof typeof StaticPath;
