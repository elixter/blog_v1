import useAuthentication from './hooks/useAuthentication';

type Props = {
	children: any;
};

const Authentication = function ({ children }: Props) {
	const { user } = useAuthentication();
	return <>{user != null && children}</>;
};
