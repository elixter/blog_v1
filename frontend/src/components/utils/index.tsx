export function sleep(ms: number) {
	// eslint-disable-next-line no-promise-executor-return
	return new Promise((r) => setTimeout(r, ms));
}

export function timeFormat(target: Date) {
	const date = new Date(target);

	return `${date.getFullYear()}년 ${date.getMonth()}월 ${date.getDay()}일`;
}
