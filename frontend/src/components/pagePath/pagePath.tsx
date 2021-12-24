export enum StaticPath {
	MAIN = '/',
}

export enum QueryPath {}

export enum DynamicPath {}

export type PagePathKey = keyof typeof StaticPath;
